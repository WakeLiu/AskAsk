<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSchoolsTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('schools', function (Blueprint $table) {
            $table->increments('id');
            $table->string('fullname')->unique();
            $table->string('name');
            $table->timestamps();

            $table->index('fullname');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::drop('schools');
    }
}
