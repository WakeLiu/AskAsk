<?php

namespace App\Repositories;

trait ImageUploadTrait
{
    public function getWebUploadPath($filename)
    {
        return asset('uploads/'.$filename);
    }
}
