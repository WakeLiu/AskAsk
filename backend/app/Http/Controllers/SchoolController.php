<?php

namespace App\Http\Controllers;

use Auth;
use App\School;
use Illuminate\Http\Request;
use App\Exceptions\ResourceException;
use Illuminate\Database\QueryException;

class SchoolController extends Controller
{
    /**
     * @api {GET} /schools 列出所有學校
     * @apiGroup Schools
     *
     * @apiPermission None
     *
     * @apiSuccess {Number} total
     * @apiSuccess {Object[]} data
     * @apiSuccess {Number} data.id
     * @apiSuccess {String} data.edu_id
     * @apiSuccess {String} data.fullname
     * @apiSuccess {String} data.name
     * @apiSuccess {String} data.district
     */
    public function index()
    {
        return [
            "total" => School::count(),
            "data" => School::all(),
        ];
    }

    /**
     * @api {POST} /schools 新增學校
     * @apiGroup Schools
     *
     * @apiPermission Admin
     * @apiParam {String} edu_id
     * @apiParam {String} fullname
     *
     * @apiUse UnauthorizedError
     * @apiUse ForbiddenError
     */
    public function store(Request $request)
    {
        $user = Auth::guard('api')->user();
        $this->authorizeForUser($user, 'store', School::class);

        $this->validate($request, [
            'edu_id' => 'required',
            'fullname' => 'required',
        ]);

        $school = new School();

        $school->edu_id = $request->edu_id;
        if ($request->has('district')) {
            $school->district = $request->district;
        }
        $school->fullname = $request->fullname;
        if ($request->has('name')) {
            $school->name = $request->name;
        }

        try {
            $school->save();
        } catch (QueryException $e) {
            throw new ResourceException('duplicate edu_id or fullname');
        }

        return $school;
    }

    public function update(Request $request, $id)
    {
        $school = School::findOrFail($id);

        $user = Auth::guard('api')->user();
        $this->authorizeForUser($user, 'update', $school);

        if ($request->has('edu_id')) {
            $school->edu_id = $request->edu_id;
        }
        if ($request->has('district')) {
            $school->district = $request->district;
        }
        if ($request->has('fullname')) {
            $school->fullname = $request->fullname;
        }
        if ($request->has('name')) {
            $school->name = $request->name;
        }

        try {
            $school->save();
        } catch (QueryException $e) {
            throw new ResourceException('duplicate edu_id or fullname');
        }

        return $school;
    }

    public function show($id)
    {
        return School::findOrFail($id);
    }

    public function destroy($id)
    {
        $school = School::findOrFail($id);

        $user = Auth::guard('api')->user();
        $this->authorizeForUser($user, 'destroy', $school);

        $school->delete();

        return ['status' => 'success'];
    }
}
