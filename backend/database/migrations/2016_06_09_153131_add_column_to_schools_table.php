<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddColumnToSchoolsTable extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::table('schools', function (Blueprint $table) {
            $table->string('edu_id');
            $table->string('district');

            $table->index('district');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down()
    {
        Schema::table('schools', function (Blueprint $table) {
            $table->dropColumn(['edu_id', 'district']);
        });
    }
}
