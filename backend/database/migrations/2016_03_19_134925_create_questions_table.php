<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateQuestionsTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('questions', function (Blueprint $table) {
            $table->increments('id');
            $table->char('type', 10);
            $table->integer('user_id')->unsigned();
            $table->text('content');
            $table->boolean('solved')->default(false);
            $table->timestamps();

            $table->index('user_id');
            $table->index('type');
            $table->index('solved');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::drop('questions');
    }
}
