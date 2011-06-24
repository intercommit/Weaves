ModalBoxInit = Class.create( {

   /* Initialize Function */
   initialize : function(spec) {
      if (spec.type == "page") {
         Event.observe($(spec.id), spec.event, function() {
            Modalbox.show(spec.href, spec.params);
         });
      } else {
         Event.observe($(spec.id), spec.event, function() {
            Tapestry.ajaxRequest(spec.href, {
               method : 'get',
               onSuccess : function(transport) {
                  var node = new Element('div').update(transport.responseJSON.content);
                  Modalbox.show(node, spec.params);
               }
            }
            );
         }
         );
      }
   }
});
