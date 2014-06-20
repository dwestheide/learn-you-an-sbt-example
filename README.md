This is the example project used in my Scala Days 2014 talk [Learn you an SBT for fun and profit](http://danielwestheide.com/talks/scaladays2014/slides/#/). 

The _Hipster Coffee Shop_ is a web shop that sells overpriced coffee to Berlin hipsters. Their manager wants to have decent release notes for every release of their web application, so the SBT build contains some custom functionality for generating them from the Git history as a markdown file that contains the list of tickets closed in the release, some diff stats, and a list of the contributors to the release.

## Usage

Call _show releaseNotesGenerate__ and pass in a valid Git tag for a release to look at the generates release notes.

Call the _releaseNotesDump_ task and pass in a valid Git tag for a release to write release notes into a markdown file.

The directory into which release notes are written can be configured by providing a setting for the _releaseNotesDir_ setting key.

The file name of the generated markdown file can be changed by providing a setting for the _releaseNotesFileName_ setting key.

The release notes functionality is implemented as an SBT autoplugin, and the sole purpose of this project is to demonstrate how to provide custom setting keys and task keys in SBT, how to implement your own tasks, using the File, IO, and Process APIs as well as parser combinators, and how to provide that functionality as an SBT auto plugin.

The functionality of the plugin is not usable in a generic way, as it makes strong assumptions about the format of release tags and about Git commit messages for closed tickets. A real-world version would likely allow configuring these things, too, and fetch the actual ticket description from an issue tracker.

To allow you to play around with this quickly, the release notes functionality is not a separate SBT project. Rather, the release notes auto plugin is simply part of the build project, under _project/src/main/scala/releasenotes_.