<?php

namespace App\Repositories;

use Storage;
use App\Image;

class ImageUploadRepository
{
    use ImageUploadTrait;

    public function getUploadDirectory()
    {
        return public_path().'/uploads/';
    }

    protected function getFilename($file)
    {
        $extension = strtolower($file->getClientOriginalExtension());
        $filename = str_random(30).'.'.$extension;

        return $filename;
    }

    /**
     * create an unsaved image from file.
     */
    public function createImageFromUploadedFile($file)
    {
        $filename = $this->getFilename($file);

        $file->move($this->getUploadDirectory(), $filename);

        $image = new Image();
        $image->filename = $filename;

        return $image;
    }

    /**
     * @param string $file_path
     */
    public function destroyImageAndFile($image)
    {
        Storage::disk('local_uploads')->delete($image->filename);
        $image->delete();
    }
}
