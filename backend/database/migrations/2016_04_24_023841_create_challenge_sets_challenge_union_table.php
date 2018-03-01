<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateChallengeSetsChallengeUnionTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('challenge_sets_challenge', function (Blueprint $table) {
            $table->integer('challenge_set_id')->unsigned();
            $table->integer('challenge_id')->unsigned();
            $table->timestamps();

            $table->primary(['challenge_set_id', 'challenge_id']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::drop('challenge_sets_challenge');
    }
}
