Weaves 3.0.0
======

A Tapestry 5.3.x component module featuring:

  * New or improved components
  * Support for multiple hibernate sessions
  * Bootstrap 3 support 
  * Jquery support 2.0.3

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
- Chosen
- EnhancedSelectBox
- (Bootstrap)Modal

#### Provided Mixins in this library: ####

 * Confirm
	A javascript popup dialog to be used for confirming actions on hyperlinks.

 * OpenTip
    A tooltip based on opentip.org
    
 * Bootstrap
    A mixin to transform standard tapestry components into bootstrapped elements.

#### Provided Services in this library: ####

 * HibernateMultiSessionManager
	A multi session / multi database service
 
 * New and improved datasources for the PagedGrid component
 	- CollectionPagedGridDataSource
    - JPAPagedGridDataSource
    - HibernatePagedGridDataSource
    
### Binaries ###

For those who cannot build the jar themselves, there is a binaries directory in which compiled and packaged jar files are available.

### Building ###

Use maven as such: mvn clean install , it will then build and test the library and place it in your local repository. From there you can create a dependency in your own Tapestry project and Weaves will be auto-loaded by Tapestry.


### Dependencies ###

This build depends on the chenillekit library originally found here : https://github.com/chenillekit/chenillekit
However that version does NOT work with Tapestry 5.3.7 and requires some additional updates to the code to function with 'Weaves'.
Weaves depends on version 1.4.0 which can be found at : https://github.com/antalk/chenillekit 


### DEMO ###

A working demo can be found at: http://intercommitweavesdemo.intercommit.cloudbees.net/, the demo runs on tomcat7 with JAVA7
