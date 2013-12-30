About this forked repo
======================

This a forked repository of https://github.com/gturri/dokuJClient


No source code has been modified, it's just a conversion from ant to maven.
Files have been moved to separate tests from others sources, and resources from java files.

dokuJClient requires a dependency which is not deploy on Maven Central, so you need to package this jar yourself.

If you just want to install it on your local repository cache
-------------------------------------------------------------

    git clone https://github.com/timroes/aXMLRPC
    cd aXMLRPC
    mvn clean install
    cd ..
    git clone https://github.com/lgringo/dokuJClient
    cd dokuJClient
    mvn clean install
    cd ..

If you have a private repository
--------------------------------

    git clone https://github.com/timroes/aXMLRPC
    cd aXMLRPC
    mvn clean package source:jar javadoc:jar deploy:deploy-file -Dfile=target/aXMLRPC-1.7.2.jar -Dsources=target/aXMLRPC-1.7.2-sources.jar -Djavadoc=target/aXMLRPC-1.7.2-javadoc.jar -DgroupId=de.timroes -DartifactId=aXMLRPC -Dversion=1.7.2 -Dpackaging=jar -Durl=<your private repo>
    cd ..
    git clone https://github.com/lgringo/dokuJClient
    cd dokuJClient
    mvn clean package source:jar javadoc:jar deploy:deploy-file -Dfile=target/dokujclient-1.0-SNAPSHOT.jar -Dsources=target/dokujclient-1.0-SNAPSHOT-sources.jar -Djavadoc=target/dokujclient-1.0-SNAPSHOT-javadoc.jar -DgroupId=fr.turri -DartifactId=dokujclient -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Durl=<your private repo>
    cd ..

For example, if you use a Nexus Repository :
	url=https://www.myserver.com/nexus/content/repositories/thirdparty/

DokuJClient
===========

Dokujclient is both a command line tool to interact with instances of Dokwiki,
and a Java library for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc).

Currently tested with:
* Binky       (dokuwiki-2013-12-08)
* Weatherwax  (dokuwiki-2013-05-10)
* Adora Belle (dokuwiki-2012-10-03)
* Angua       (dokuwiki-2012-01-25b)

See the "Compatibility" section for more info

Command line tool
=================

Getting started
---------------

Here's a glimpse of what this tool can do:

    dokujclient --user myUser --password myPassword --url http://mywiki/lib/lib/exe/xmlrpc.php getTitle
    > myWiki title

    dokujclient help
    > [(-u|--user) <user>] --url <url> [(-p|--password) <password>] [-h|--help] [--version] command
    >
    > Available commands:
    > [...skipped...]

    #put user, password, and url, in the config file
    vim ~/.dokujclientrc

    #get the list of pages of all the wiki
    dokujclient getPagelist .
    > [...skipped...]

    dokujclient appendPage builds:synthesis "Build launched at 12:23 took 3'24"
    dokujclient getPage builds:synthesis
    > Build launched at 11:12 took 3'19
    > Build launched at 12:23 took 3'24

    #help command can give information about a given command
    dokujclient help putAttachment
    > Syntax for putAttachment: [-f|--force] <attachmentId> <localFile>

    dokujclient putAttachment some:file.jpg ~/results.jpg

Just make sure that your wiki is configured so that the xmlrpc interface is enabled, and so that your user is allowed to use it (ie: "remote" and "remoteuser" entries in your configuration).

Installation
------------
* Download the [binaries](http://turri.fr/dokujclient).
* Extract it, and add the extracted directoy to your path
* Ensure it's correctly installed, typing e.g.:

    dokujclient --version

Config file
-----------
To avoid typing your url, user, and password each time, you may create in your home a .dokujclientrc,
and put some or all of this info in it.

    echo "url=http://myhost/mywiki/lib/exe/xmlrpc.php" > ~/.dokujclientrc
    echo "user=toto" >> ~/.dokujclientrc
    echo "password=myPassword" >> ~/.dokujclientrc


dokuJClient.jar
==========

If you want to build your own application, if you don't want to deal with xmlrpc request yourself,
or if you don't want to handle the different versions of Dokuwiki, you may use this library.

Getting started
---------------
Everything is done through the DokuJClient: just create one and play with its methods.
Here is a quick example which displays the title of the wiki and the list of its pages:

    import dw.xmlrpc.DokuJClient;
    import dw.xmlrpc.Page;
    
    public class Main {
      public static void main(String[] args) throws Exception{
        String url = "http://mywiki/lib/exe/xmlrpc.php";
        String user = "myUser";
        String pwd = "myPassword";

        DokuJClient client = new DokuJClient(url, user, pwd);
        System.out.println("Pages in the wiki " + client.getTitle() + " are:");
        for(Page page : client.getAllPages()){
          System.out.println(page.id());
        }

      }
    }

Make sure to add the jar listed in the Dependencies section below, as well as dokujclient.jar to your classpath.
Also make sure to configure your wiki so that xmlrpc interface is enabled, and so that your user is
allowed to use it (ie: "remote" and "remoteuser" entries in your configuration)

Getting the binaries
--------------------
Binaries may be [downloaded](http://turri.fr/dokujclient) directly.


Running integration tests
--------------------------
To run the tests you'll need junit 4.
You will also need to set up a fake wiki.
Please see src/dw/xmlrpc/itest/README.md to know how to set it up.


After that, to run the tests, just run, at the root of the repo:

    ant junit


or

    ant junitreport

if you have [Cobertura](http://cobertura.sourceforge.net/introduction.html) installed you
may also compute test coverage with:

    ant junitfullreport

You may want to run the tests in Eclipse. If so, please set the current working directory to "src".
ie: in the 'Run Configurations' window, choose its 'Arguments' tab, and set the
'Working directory' to '${workspace_loc:dokuJClient/src}

Compatibility
=============
dokuJClient aims at providing the same behavior for every supported version of Dokuwiki.
There are however, some discrepancies:

* Angua (dokuwiki-2012-01-25b)
** getAttachmentInfo can't retrieve the page title. It instead sets it to the page id.
