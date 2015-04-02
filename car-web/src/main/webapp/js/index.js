/**
 * Created by CoolGuy on 2015/3/30.
 */
var Pomelo = Pomelo || {};
var theUrl = "../search.json";
$(function(){
    //search框
    var sLeft = $(".input_left").width();
    var sRight = $(".input_right").width();
    var sAll = $(".search_div").width();
    var sRest = sAll-sLeft-sRight;
    $(".input_center").width(sRest);
    $("#search").width(sRest);


    //获取数据
    Pomelo.getData();
});

Pomelo.getData = function(){
    var _this = this;
    var searchInput = $("#search");
    Pomelo.getSearchData = function(keyword){
      var send = {
          keyword:keyword
      };
      $.getJSON(theUrl,{data:send},function(data){
          console.log(data);
      })
    };
    $(".input_right").on("click",function(){
        var searchVal = $.trim(searchInput.val());
        _this.getSearchData(searchVal);
    });


};