<?php

Route::group(['prefix' => 'su'], function () {
    Route::post('users/{user_id}', 'SuController@asUser');
    Route::post('students/{student_id}', 'SuController@asStudent');
    Route::post('volunteers/{volunteer_id}', 'SuController@asVolunteer');
});
