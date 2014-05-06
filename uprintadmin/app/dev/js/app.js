"use strict";
angular.module("uprintadmin", [
  "templates-main",
  "ui.router",
  "ui.router.state",
  "ui.bootstrap",
  "uprintadmin.home", "uprintadmin.printers", "uprintadmin.downloads", "uprintadmin.authService"])
.config(["$locationProvider", function($locationProvider){
  
}])
.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  authenticated: 'authenticated',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
})
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
