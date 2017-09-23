'use strict';

const SockJS = require('sockjs-client');
require('stompjs');

function register(registrations) {
    //  WebSocket is pointed at the application’s /payroll endpoint.
    const socket = SockJS('/payroll');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        // Iterate over the array of registrations supplied so each can subscribe for callback as messages arrive.
        registrations.forEach(function (registration) {
            stompClient.subscribe(registration.route, registration.callback);
        });
    });
}

module.exports.register = register;