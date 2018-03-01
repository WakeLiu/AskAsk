<?php

namespace App\Http\Controllers;

use Auth;
use App\User;
use App\Volunteer;
use Illuminate\Http\Request;
use App\Factories\UserFactory;
use App\Repositories\ImageUploadRepository;

class VolunteerController extends ApiController
{
    protected $userFactory;
    protected $image;

    public function __construct(UserFactory $userFactory, ImageUploadRepository $image)
    {
        $this->userFactory = $userFactory;
        $this->image = $image;
    }

    /**
     * @api {GET} /volunteer List all volunteers
     * @apiGroup Volunteer
     *
     * @apiPermission Admin
     * @apiPermission Student
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     */
    public function index()
    {
        $this->authorize('index', Volunteer::class);
        $user = Auth::guard('api')->user();

        if ($user->isAdmin()) {

            return Volunteer::with(['user', 'photo'])->orderBy('name')->get();
        } else if ($user->isStudent()) {

            return Volunteer::select(['id', 'name', 'gender'])->paginate(15);
        }
    }

    /**
     * @api {GET} /volunteer/:id show volunteers
     * @apiGroup Volunteer
     *
     * @apiPermission Admin
     * @apiPermission Student
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     */
    public function show($id)
    {
        $this->authorize('show', Volunteer::class);

        $user = Auth::guard('api')->user();

        if ($user->isAdmin()) {

            return Volunteer::with(['user', 'photo'])->findOrFail($id);
        } else if ($user->isStudent()) {

            return Volunteer::select(['id', 'name', 'gender'])->findOrFail($id);
        }
    }

    public function store(Request $request)
    {
        $this->authorize('store', Volunteer::class);

        $this->validate($request, [
            'email' => 'required',
            'password' => 'required',
        ]);

        $volunteer = new Volunteer();
        if ($request->has('name')) {
            $volunteer->name = $request->input('name');
        }
        if ($request->has('profile')) {
            $volunteer->profile = $request->input('profile');
        }

        if ($request->hasFile('photo_file')) {
            $file = $request->file('photo_file');

            $image = $this->image->createImageFromUploadedFile($file);
            $image->save();

            $volunteer->photo()->associate($image);
        }

        $volunteer->save();

        $user = new User();
        $user->email = $request->email;
        $user->password = bcrypt($request->password);
        $user->userable()->associate($volunteer);
        $user->save();

        return $volunteer->load('user', 'photo');
    }

    public function update(Request $request, $id)
    {
        $volunteer = Volunteer::findOrFail($id);

        if ($request->has('name')) {
            $volunteer->name = $request->input('name');
        }

        if ($request->has('profile')) {
            $volunteer->profile = $request->input('profile');
        }

        if ($request->hasFile('photo_file')) {
            $file = $request->file('photo_file');

            $image = $this->image->createImageFromUploadedFile($file);
            $image->save();

            $volunteer->photo()->associate($image);
        }

        $volunteer->save();

        return $volunteer->load(['user', 'photo']);
    }

    public function updatePhoto(Request $request, $id)
    {
        $this->validate($request, [
            'photo_file' => 'required|image',
        ]);

        $volunteer = Volunteer::findOrFail($id);

        $file = $request->file('photo_file');

        $image = $this->image->createImageFromUploadedFile($file);
        $image->save();

        $volunteer->photo()->associate($image);
        $volunteer->save();

        return $volunteer->load(['user', 'photo']);
    }
}
