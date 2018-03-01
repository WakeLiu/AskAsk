<?php

Route::get('/', function () {
    return view('welcome');
});

Route::group(['middleware' => ['web']], function () {
    // TODO csrf token problem
    Route::post('login', 'Auth\AuthController@postLogin');
});

Route::group([
    'prefix' => 'api/v1',
    'middleware' => ['cors'],
], function () {
    /*
     * Our API based service
     * all routes that can be CORS accessed
     */

    Route::get('schools', 'SchoolController@index');
    Route::post('login', 'Auth\JWTAuthController@emailLogin');
    Route::post('register', 'Auth\JWTAuthController@register');

    // the API protected by api_token are listed here
    Route::group(['middleware' => 'auth:api'], function () {
        require(__DIR__ . '/api_auth_routes.php');

        Route::group(['prefix' => 'schools'], function () {
            Route::post('/', 'SchoolController@store');
            Route::get('{school}', 'SchoolController@show');
            Route::patch('{school}', 'SchoolController@update');
            Route::delete('{school}', 'SchoolController@destroy');
        });

        Route::group(['prefix' => 'districts'], function () {
            Route::get('/', 'DistrictController@index');
            Route::get('{district}/schools', 'District\SchoolController@index');
        });

        Route::group(['prefix' => 'challenge_questions'], function () {
            Route::get('/', 'ChallengeQuestionController@index');
            Route::get('{challenge_question}', 'ChallengeQuestionController@show');
            Route::post('/', 'ChallengeQuestionController@store');
            Route::patch('{challenge_question}', 'ChallengeQuestionController@update');
            Route::delete('{challenge_question}', 'ChallengeQuestionController@destroy');
        });

        Route::group(['prefix' => 'tags'], function () {
            Route::get('/', 'TagController@index');
            Route::get('{id}', 'TagController@show')->where('id', '[0-9]+');
            Route::get('{name}', 'TagController@showByName');
            Route::post('/', 'TagController@store');
            Route::delete('{id}', 'TagController@destroy')->where('id', '[0-9]+');
            Route::delete('{name}', 'TagController@destroyByName');
        });

        Route::group(['prefix' => 'users'], function () {
            Route::get('students', 'StudentController@index');
            Route::post('students', 'StudentController@store');
            Route::post('volunteers', 'VolunteerController@store');
            Route::post('admins', 'AdminController@store');
        });

        // alias /me for /user/{myself}
        Route::group(['prefix' => 'me'], function () {
            Route::get('questions', 'Me\QuestionController@index');
        });

        require(__DIR__ . '/api_su_routes.php');
    });
});
