<?php

Route::group(['prefix' => 'digitalized_questions'], function () {
    Route::get('/', 'DigitalizedQuestionController@index');
    Route::post('/', 'DigitalizedQuestionController@store');
});

Route::group(['prefix' => 'students'], function () {
    Route::get('/', 'StudentController@index');
    Route::post('/', 'StudentController@store');
    Route::get('/{id}', 'StudentController@show');
    Route::patch('/{id}', 'StudentController@update');
    Route::post('/{id}/photo', 'StudentController@updatePhoto');
    Route::patch('/{id}/status', 'StudentController@updateStatus');
});

Route::group(['prefix' => 'volunteers'], function () {
    Route::get('/', 'VolunteerController@index');
    Route::get('{id}', 'VolunteerController@show');
    Route::post('/', 'VolunteerController@store');
    Route::patch('/{id}', 'VolunteerController@update');
    Route::patch('/{id}/photo', 'VolunteerController@updatePhoto');
});

Route::group(['prefix' => 'me'], function () {
    Route::get('/', 'MeController@show');
});

Route::group(['prefix' => 'questions'], function () {
    Route::get('/', 'QuestionController@index');

    Route::get('{question}', 'QuestionController@show');
    Route::post('/', 'QuestionController@store');
    Route::patch('{question}', 'QuestionController@update');
    Route::patch('{question}/solved', 'QuestionController@updateSolved');
    Route::delete('{question}', 'QuestionController@destroy');

    Route::post('{question}/answer', 'Question\AnswerController@store');
    Route::patch('{question}/answer', 'Question\AnswerController@update');

    Route::post('{question}/images', 'Question\ImageController@store');
    Route::delete('{question}/images/{image_id}', 'Question\ImageController@destroy');

    Route::post('{question}/tags', 'Question\TagController@store');
    Route::delete('{question}/tags/{tag_id}', 'Question\TagController@destroy');

    Route::post('{question}/digitalized_question', 'Question\DigitalizedQuestionController@store');
    Route::patch('{question}/digitalized_question', 'Question\DigitalizedQuestionController@update');
});
