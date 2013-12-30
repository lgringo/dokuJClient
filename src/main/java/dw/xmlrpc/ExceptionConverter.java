package dw.xmlrpc;

import de.timroes.axmlrpc.XMLRPCException;
import dw.xmlrpc.exception.*;

//! @cond

/**
 * Converts an XmlRpcException into a the most relevant DokuException.
 */
class ExceptionConverter {
	public static DokuException Convert(XMLRPCException e, String url){
		String message = e.getMessage();
		if ( message.contains("The page is currently locked") ){
			return new DokuPageLockedException(e);
		}
		if ( message.contains("The XMLRPC call timed out.")){
			return new DokuTimeoutException(e);
		}
		if ( message.contains("not authorized to call method")
				|| message.contains("forbidden to call the method")
				|| message.contains("You are not allowed to") //for DW up to 2012-01-25b
				|| message.contains("You don't have permissions to delete files") //for DW up to 2012-01-25b
				){
			return new DokuUnauthorizedException(e);
		}
		if ( message.contains("java.io.FileNotFoundException")
				|| message.contains("The server responded with a http 301 or 302 status code")){
			return buildGenericBadUrlException(e);
		}
		if ( e.getCause() != null && e.getCause().getClass() == java.net.UnknownHostException.class ){
			String mess = "Host doesn't exist. Check url";
			return new DokuBadUrlException(mess, e);
		}

		if ( message.contains("Positive wordblock check")){
			return new DokuWordblockException(e);
		}
		if ( message.contains("HTTP server returned unexpected status: Forbidden")){
			return new DokuUnauthorizedException(e);
		}
		if ( message.contains("Could not delete file")){
			return new DokuDeleteAttachmentException(e);
		}
		if ( message.contains("The requested file does not exist")){
			return new DokuDistantFileDoesntExistException(e);
		}
		if ( message.contains("File is still referenced")){
			return new DokuAttachmentStillReferenced(e);
		}
		if ( message.contains("The provided value is not a valid timestamp")){
			return new DokuInvalidTimeStampException(e);
		}
		if ( message.contains("There are no changes in the specified timeframe")){
			return new DokuNoChangesException(e);
		}
		if ( message.contains("Refusing to write an empty new wiki page")){
			return new DokuEmptyNewPageException(e);
		}

		//Won't match if the wiki's locale isn't 'en'
		if ( message.contains("Upload denied. This file extension is forbidden!")
				|| ( message.contains("The uploaded content did not match the ") && message.contains("file extension."))
				|| message.contains("File already exists. Nothing done.")){
			return new DokuAttachmentUploadException(message, e);
		}

		//If we reach this point, we don't know what went wrong.
		//We try a final educated guess before giving up
		if ( ! url.endsWith("/lib/exe/xmlrpc.php") ){
			return buildGenericBadUrlException(e);
		}

		return new DokuUnknownException(e);
	}

	private static DokuBadUrlException buildGenericBadUrlException(Throwable e){
		String mess = "Couldn't find the xmlrpc interface. Make sure url looks like http[s]://server/mywiki/lib/exe/xmlrpc.php";
		return new DokuBadUrlException(mess, e);
	}
//! @endcond
}
