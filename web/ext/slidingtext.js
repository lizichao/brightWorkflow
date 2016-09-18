Ext.BLANK_IMAGE_URL = 'resources/images/default/s.gif';   
    
//this runs on DOM load - you can access all the good stuff now.   
Ext.onReady(function() {   
    
//simple function to slide text up and down and optionally hide..   
var slideText = function(direction,element){   
    
var slideMe = Ext.get(element);   
    
switch(direction){   
    //determine the direction of travel   
    case 'up' :   
            //lets check to see if this is visible    
                        //and if not then its already hidden :)   
            if (slideMe.isVisible()) {   
                //if we get here then the element is visible   
                slideMe.slideOut('t', {   
                    easing: 'easeOut',   
                    duration: .5,   
                    remove: false,   
                    useDisplay: true  
                });   
            }   
        break;   
    case 'down' :   
            //lets check to see if this is visible and    
                        //if it is then we do nothing :)   
            if (!slideMe.isVisible()) {   
                //if we get here then the element is visible   
                slideMe.slideIn('t', {   
                    easing: 'easeOut',   
                    duration: .5   
                });   
            }          
        break;   
    default :   
        //the default action is simply to toggle the element   
        slideMe.toggle();   
        break  
    
}   
//ends the slider function     
}   
    
Ext.get('textup').on('click',function(e,t){   
    //simple slide of this element   
    slideText('up','slider');   
    Ext.get(t.id).frame('cccccc',1);   
});   
    
Ext.get('textdown').on('click',function(e,t){   
    //simple slide of this element   
    slideText('down','slider');   
    Ext.get(t.id).frame('cccccc',1);   
});   
    
Ext.get('texttoggle').on('click',function(e,t){   
    //simple toggle of this element   
    slideText('toggle','slider');   
    Ext.get(t.id).frame('cccccc',1);   
});   
}); 