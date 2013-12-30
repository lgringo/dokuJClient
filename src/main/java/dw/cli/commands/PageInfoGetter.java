package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageInfo;
import dw.xmlrpc.exception.DokuException;

public class PageInfoGetter extends Command {
	private final boolean _withVersion;

	public PageInfoGetter(){
		this(false);
	}

	public PageInfoGetter(boolean withVersion){
		_withVersion = withVersion;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);

		if (_withVersion){
			jsap.registerParameter(new UnflaggedOption("timestamp").setStringParser(JSAP.INTEGER_PARSER).setRequired(true));
		}
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		String pageId = _config.getString("pageId");
		PageInfo pageInfo;

		if ( _withVersion ){
			int timestamp = _config.getInt("timestamp");
			pageInfo = dokuClient.getPageInfoVersion(pageId, timestamp);
		} else {
			pageInfo = dokuClient.getPageInfo(pageId);
		}

		return new Output(pageInfoToString(pageInfo));
	}

	private String pageInfoToString(PageInfo pageInfo){
		return pageInfo.id()
				+ " " + pageInfo.version()
				+ " " + pageInfo.author();
	}
}
