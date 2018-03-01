<?php

namespace App;

use App\Repositories\ImageUploadTrait;
use Illuminate\Database\Eloquent\Model;

class Image extends Model
{
    use ImageUploadTrait;

    public function imageable()
    {
        return $this->morphTo();
    }

    public function getFilenameAttribute($value)
    {
        return $this->getWebUploadPath($value);
    }
}
