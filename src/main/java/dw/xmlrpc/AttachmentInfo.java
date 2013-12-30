package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a file uploaded in the wiki as getAttachmentInfo would
 */
public class AttachmentInfo {
	private final String _id;

	/**
	 * Media id (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private final Integer _size;

	/**
	 * Size in bytes
	 */
	public Integer size(){
		return _size;
	}

	private final Date _lastModified;

	/**
	 * Date of last modification of the file
	 */
	public Date  lastModified(){
		return _lastModified;
	}

	public AttachmentInfo(String id, Integer size, Date lastModified){
		_id = id;
		_size = size;
		_lastModified = lastModified;
	}

	@Override
	public String toString(){
		return "id: " + (_id == null ? "null" : _id)
				+ ", size:" + (_size == null ? "unknown" : _size)
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString());
	}
}
