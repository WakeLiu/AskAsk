<?php

/*
|--------------------------------------------------------------------------
| Model Factories
|--------------------------------------------------------------------------
|
| Here you may define all of your model factories. Model factories give
| you a convenient way to create models for testing and seeding your
| database. Just tell the factory how a default model should look.
|
 */

$factory->define(App\Admin::class, function (Faker\Generator $faker) {
    return [
    ];
});

$factory->define(App\Answer::class, function (Faker\Generator $faker) {
    return [
        'content' => $faker->sentence,
    ];
});

$factory->define(App\ChallengeQuestion::class, function (Faker\Generator $faker) {
    return [
        'content' => $faker->sentence,
    ];
});

$factory->define(App\DigitalizedQuestion::class, function (Faker\Generator $faker) {
    return [
        'content' => $faker->sentence,
    ];
});

$factory->define(App\Image::class, function (Faker\Generator $faker) {
    return [
        'filename' => $faker->name,
    ];
});

$factory->define(App\Question::class, function (Faker\Generator $faker) {
    return [
        'content' => $faker->sentence,
        'solved' => false,
    ];
});

$factory->define(App\School::class, function (Faker\Generator $faker) {
    return [
        'fullname' => $faker->name,
        'name' => $faker->name,
        'edu_id' => str_random(6),
    ];
});

$factory->define(App\Student::class, function (Faker\Generator $faker) {
    return [
        'name' => $faker->name,
        'gender' => 'MAN',
    ];
});

$factory->define(App\Tag::class, function (Faker\Generator $faker) {
    return [
        'name' => $faker->name,
        'type' => 'none',
    ];
});

$factory->define(App\User::class, function (Faker\Generator $faker) {
    return [
        'email' => $faker->safeEmail,
        'password' => bcrypt(str_random(10)),
        'remember_token' => str_random(10),
    ];
});

$factory->defineAs(App\User::class, 'admin', function (Faker\Generator $faker) {
    return [
        'email' => $faker->safeEmail,
        'password' => bcrypt(str_random(10)),
        'remember_token' => str_random(10),
        'userable_type' => App\Admin::class,
    ];
});

$factory->define(App\Volunteer::class, function (Faker\Generator $faker) {
    return [
        'name' => $faker->name,
        'profile' => $faker->sentence,
    ];
});
