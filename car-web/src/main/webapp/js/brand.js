/**
 * Created by Administrator on 15-4-1.
 */
$(function(){
    //search框
    var sLeft = $(".input_left").width();
    var sRight = $(".input_right").width();
    var sAll = $(".search_div").width();
    var sRest = sAll-sLeft-sRight;
    $(".input_center").width(sRest);
    $("#search").width(sRest);

    
    //获取数据
    //Pomelo.getData();
    getBrandName();
});

function getBrandName() {
	var value = ["全国品牌","中国品牌","中国品牌","中国品牌"];
	
	var id =  $("#title").attr("value");
	$("#title").text(value[id]);
	//alert("id = " + value[id]);
	
}

