angular.module("uprintadmin.downloads", ["ui.router.state"]).config(["$stateProvider",function($stateProvider){
  $stateProvider.state('downloads', {
    url: '/downloads',
    views: {
      "main": {
        controller: "DownloadsCtrl",
        templateUrl: "../downloads/downloads.tpl.html"
      }
    },
    data:{ pageTitle: 'Downloads' }
  })
}]).controller("DownloadsCtrl", ["$scope", "$log", "$state", function($scope,$log, $state){
  $log.info("in the DownloadsCtrl");
  
}])
;