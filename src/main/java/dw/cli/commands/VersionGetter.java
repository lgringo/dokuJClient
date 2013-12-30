package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class VersionGetter extends SimpleCommand {
	@Override
	protected String query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getVersion();
	}
}
