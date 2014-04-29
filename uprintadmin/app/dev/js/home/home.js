"use strict";
angular.module("uprintadmin.home",["ui.router.state"]).config(["$stateProvider",function($stateProvider){
  $stateProvider.state('home', {
    url: '/home',
    views: {
      "main": {
        controller: "HomeCtrl",
        templateUrl: "../home/home.tpl.html"
      }
    },
    data:{ pageTitle: 'Home' }
  })
}]).controller("HomeCtrl", ["$scope","$log","$state", function($scope,$log,$state){
  $log.info("in the HomeCtrl");
  $log.info($state);
}])
;
