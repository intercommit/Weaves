Weaves
======

A Tapestry 5.2 component module featuring new components and multiple database support.

#### Provided components in this library: ####

- PagedGrid
- PopupWindow
- Switch
- TextMarker
- ModalBox
- Hoverlink
- EditableSelectBox
- DropDownMenu

#### Provided Mixins in this library: ####

 * Confirm
	A javascript popup dialog to be used for confirming actions on urls


#### Provided Services in this library: ####

 * HibernateMultiSessionManager
	A multi session / multi database service

	
### TODO ###

There are still some TODO's left in the code, but 95% is working fine.

### Building ###

Use maven as such: mvn clean install , it will then build and test the library and place it in your local repository. From there you can create a dependency in your own Tapestry project and Weaves will be auto-loaded by Tapestry.

### Testing ###

Currently at around 70% coverage. Still some work to do to increase coverage.

### DEMO ###

A working demo can be found at: http://intercommitweavesdemo.intercommit.cloudbees.net/
There are 3 parts still not (correctly) working at the demo site: 

 * TextMarker demo - Crashes with a internal Tapestry exception
 * ModalBox demo - Crashes with a internal Tapestry exception
 * Hibernate demo - Seems to ignore the Layout component