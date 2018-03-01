<?php

namespace App\Factories;

class UserFactory
{
    public function generateApiToken()
    {
        return str_random(32);
    }
}
