angular.module('templates-main', ['../downloads/downloads.tpl.html', '../home/home.tpl.html', '../login/login.tpl.html', '../printers/printers.tpl.html']);

angular.module("../downloads/downloads.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../downloads/downloads.tpl.html",
    "<h1>Downloads</h1>");
}]);

angular.module("../home/home.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../home/home.tpl.html",
    "<h1 class=cover-heading>Home page.</h1><p class=lead>Cover is a one-page template for building simple and beautiful home pages. Download, edit the text, and add your own fullscreen background photo to make it your own.</p><p class=lead><a href=# class=\"btn btn-lg btn-default\">Learn more</a></p>");
}]);

angular.module("../login/login.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../login/login.tpl.html",
    "<h1>login</h1>");
}]);

angular.module("../printers/printers.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../printers/printers.tpl.html",
    "<h1>PRINTERS</h1>");
}]);
