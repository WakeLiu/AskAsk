<?php

namespace App\Http\Controllers\Question;

use App\Image;
use App\Question;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Repositories\ImageUploadRepository;

class ImageController extends Controller
{
    protected $image;

    public function __construct(ImageUploadRepository $image)
    {
        $this->image = $image;
    }

    public function store(Request $request, $question_id)
    {
        $this->validate($request, [
            'file' => 'required|image',
        ]);

        $question = Question::findOrFail($question_id);

        $file = $request->file('file');
        $image = $this->image->createImageFromUploadedFile($file);

        $question->images()->save($image);

        return $image;
    }

    public function destroy(Request $request, $question_id, $image_id)
    {
        $question = Question::findOrFail($question_id);
        $image = $question->images()->findOrFail($image_id);

        $this->image->destroyImageAndFile($image);

        return ['status' => 'success'];
    }
}
