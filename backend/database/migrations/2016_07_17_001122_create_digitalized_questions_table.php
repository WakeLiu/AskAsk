<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateDigitalizedQuestionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('digitalized_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->char('type', 10);
            $table->integer('user_id')->unsigned();
            $table->text('content');
            $table->string('grade');
            $table->integer('question_id')->unsigned();
            $table->timestamps();

            $table->index('user_id');
            $table->index('question_id');
            $table->index('type');
            $table->index('grade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('digitalized_questions');
    }
}
