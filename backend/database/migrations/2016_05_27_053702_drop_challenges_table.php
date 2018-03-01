<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class DropChallengesTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::drop('challenges');
        Schema::drop('challenge_sets');
        Schema::drop('challenge_sets_challenge');
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::create('challenges', function (Blueprint $table) {});
        Schema::create('challenge_sets', function (Blueprint $table) {});
        Schema::create('challenge_sets_challenge', function (Blueprint $table) {});
    }
}
