"use strict";
angular.module("uprintadmin.printers",["ui.router.state"]).config(["$stateProvider",function($stateProvider){
  $stateProvider.state('printers', {
    url: '/printers',
    views: {
      "main": {
        controller: "PrintersCtrl",
        templateUrl: "../printers/printers.tpl.html"
      }
    },
    data:{ pageTitle: 'Printers Available' }
  })
}]).controller("PrintersCtrl", ["$scope", "$log", "$state", function($scope,$log, $state){
  $log.info("in the PrintersCtrl");
  
}])
;