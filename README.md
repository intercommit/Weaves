Weaves
======

A Tapestry 5.3.x component module featuring new components and adds support for multiple hibernate sessions.

#### Provided components in this library: ####

- PagedGrid
- PopupWindow
- Switch
- TextMarker
- ModalBox
- Datatable
- EditableSelectBox
- DropDownMenu
- Growler
- Tabs

#### Provided Mixins in this library: ####

 * Confirm
	A javascript popup dialog to be used for confirming actions on hyperlinks.

 * OpenTip
    A tooltip based on opentip.org

#### Provided Services in this library: ####

 * HibernateMultiSessionManager
	A multi session / multi database service

	
### Binaries ###

For those who cannot build the jar themselves, there is a binaries directory in which compiled and packaged jar files are available.

### Building ###

Use maven as such: mvn clean install , it will then build and test the library and place it in your local repository. From there you can create a dependency in your own Tapestry project and Weaves will be auto-loaded by Tapestry.

### Testing ###

Currently at around 70% coverage. Still some work to do to increase coverage.

### DEMO ###

A working demo can be found at: http://intercommitweavesdemo.intercommit.cloudbees.net/
