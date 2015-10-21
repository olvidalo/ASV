
var IMG_OPEN = '';
var IMG_CLOSED = '';

function init_viewer(o_img, c_img) {
    IMG_OPEN = o_img;
    IMG_CLOSED = c_img;
}

function expand_highlighted_imdi() {
    $(".searchword").parents('.IMDI_group_dynamic.imdi').css('display','block');
}

function is_open(id) {
    var GECKO = document.getElementById ? 1 : 0;
    var NS = document.layers ? 1 : 0;
    var IE = document.all ? 1 : 0;
    var opened = 0;
    if (GECKO) {
        opened = (document.getElementById(id).style.display == 'block');
    }
    else if (NS) {
        opened = (document.layers[id].display == 'block');
    }
    else if (IE) {
        opened = (document.all[id].style.display == 'block');
    }
    return opened;
}
function get_element(id) {
    var GECKO = document.getElementById ? 1 : 0;
    var NS = document.layers ? 1 : 0;
    var IE = document.all ? 1 : 0;
    var elem = null;
    if (GECKO) {
        elem = document.getElementById(id);
    }
    else if (NS) {
        elem = document.layers[id];
    }
    else if (IE) {
        elem = document.all[id];
    }
    return elem;
}

function change_status(id) {
    var GECKO = document.getElementById ? 1 : 0;
    var NS = document.layers ? 1 : 0;
    var IE = document.all ? 1 : 0;
    var status;
    var img_src;
    if (is_open(id)) {
        status = 'none';
        img_src = IMG_CLOSED;
    }
    else {
        status = 'block';
        img_src = IMG_OPEN;
    }
    if (GECKO) {
        document.getElementById(id).style.display = status;
    }
    else if (NS) {
        document.layers[id].display = status;
    }
    else if (IE) {
        document.all[id].style.display = status;
    }
    var img_id = 'img_' + id;
    var img_elem = get_element(img_id);
    img_elem.src = img_src;
}