"use strict";
angular.module("uprintadmin", [
  "templates-main",
  "ui.router",
  "ui.router.state",
  "ui.bootstrap",
  "uprintadmin.home", "uprintadmin.printers", "uprintadmin.downloads"])
.config(["$locationProvider", function($locationProvider){
  
}])
.run(["$state", "$stateParams", "$rootScope", function($state, $stateParams, $rootScope){

}])
.controller("AppCtrl", ["$scope", "$state","$modal","$http","$log",function($scope,$state,$modal,$http,$log){
  $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
    if ( angular.isDefined( toState.data.pageTitle ) ) {
      $scope.pageTitle = toState.data.pageTitle + ' | UprintAdmin' ;
    }
  });

  $scope.state = $state;

  $scope.copyrightDate = new Date();

}])
;
