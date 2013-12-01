package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.SearchResult;
import dw.xmlrpc.exception.DokuException;

public class Searcher extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("searchQuery").setRequired(true));
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
		jsap.registerParameter(new Switch("snippet").setLongFlag("snippet"));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		List<SearchResult> searchResults = dokuClient.search(config.getString("searchQuery"));
		return new Output(searchResultsToString(searchResults, config.getBoolean("longFormat"), config.getBoolean("snippet")));
	}

	private String searchResultsToString(List<SearchResult> searchResults,	boolean longFormat, boolean withSnippet) {
		LineConcater concater = new LineConcater();

		for(SearchResult searchResult : searchResults){
			if ( longFormat ){
				concater.addLine(searchResultToLongString(searchResult));
			} else {
				concater.addLine(searchResultToString(searchResult));
			}

			if ( withSnippet ){
				addSnippet(concater, searchResult);
			}
		}

		return concater.toString();
	}

	private void addSnippet(LineConcater concater, SearchResult searchResult) {
		for ( String line : searchResult.snippet().split("\n")){
			concater.addLine("> " + line);
		}
		concater.addLine("");
	}

	private String searchResultToString(SearchResult searchResult) {
		return searchResult.id();
	}

	private String searchResultToLongString(SearchResult searchResult) {
		return searchResult.score()
				+ " " + searchResult.mtime()
			    + " " + searchResult.rev()
			    + " " + searchResult.title()
			    + " " + searchResult.size()
			    + " " + searchResult.id();
	}

}
