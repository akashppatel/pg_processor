angular.module('app', [])

    .constant('apiUriPrefix', '/pg-api')

    .controller('mainCtrl', function ($http, $interval, apiUriPrefix) {

        var self = this;

        self.user = {name : null}

        self.purchase = function (user) {
                    console.log('purchasing ...... ' + user.name);

                    $http.post(apiUriPrefix + '/purchase', user).then(
                        function (response) {
                            console.log('In app.js -> purchasing : ' + response);
                            self.pgResponse = response.data;
                        },
                        function (err) {
                            console.log('In app.js -> error while purchasing : ' + err);
                        });
                };
    });