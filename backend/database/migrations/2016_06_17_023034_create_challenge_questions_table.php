<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateChallengeQuestionsTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('challenge_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->char('type', 10);
            $table->integer('user_id')->unsigned();
            $table->text('content');
            $table->timestamps();

            $table->index('user_id');
            $table->index('type');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::drop('challenge_questions');
    }
}
