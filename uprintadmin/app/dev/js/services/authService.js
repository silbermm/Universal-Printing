angular.module('uprintadmin.authService', []).factory('AuthService', ['$http', function ($http) {
  return {   
    isAuthenticated: function () {
      return $http
        .get('/auth/loggedin')
        .then(function (res, status) {
          if(status == 200){
            return true;
          }
          return false;
        });
    },
    isAuthorized: function (authorizedRoles) {
      if (!angular.isArray(authorizedRoles)) {
        authorizedRoles = [authorizedRoles];
      }
      return (this.isAuthenticated() &&
        authorizedRoles.indexOf(Session.userRole) !== -1);
    }
  };
}])