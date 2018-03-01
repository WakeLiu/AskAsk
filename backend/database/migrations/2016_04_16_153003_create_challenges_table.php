<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateChallengesTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('challenges', function (Blueprint $table) {
            $table->increments('id');
            $table->char('type', 10);
            $table->integer('user_id')->unsigned();
            $table->text('content');
            $table->integer('question_id')->unsigned();
            $table->timestamps();

            $table->index('question_id');
            $table->index('type');
            $table->index('user_id');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::drop('challenges');
    }
}
