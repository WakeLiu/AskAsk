<?php

namespace Tests;

use Illuminate\Foundation\Testing\DatabaseTransactions;

class StudentTest extends TestCase
{
    use DatabaseTransactions;

    // Index API
    public function testIndex()
    {
        FakeFactory::fakeStudent();
        FakeFactory::fakeStudent();

        $admin = FakeFactory::fakeAdmin();
        $this->actingAs($admin->user, 'api')
            ->json('GET', '/students')
            ->seeJsonStructure([
                'data' => [
                    '*' => [
                        'id',
                        'user',
                        'school',
                        'photo',
                    ],
                ],
            ]);

        $volunteer = FakeFactory::fakeVolunteer();
        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/students')
            ->seeJsonError(403);

        $student = FakeFactory::fakeStudent();
        $this->actingAs($student->user, 'api')
            ->json('GET', '/students')
            ->seeJsonError(403);
    }

    public function testShow()
    {
        $fake_student = FakeFactory::fakeStudent();
        $fake_student->gender = 'M';
        $fake_student->name = 'test';
        $fake_student->save();

        $admin = FakeFactory::fakeAdmin();
        $this->actingAs($admin->user, 'api')
            ->json('GET', '/students/' . $fake_student->id)
            ->seeJsonStructure([
                'id',
                'user',
                'school',
                'photo',
            ])
            ->seeJson([
                'gender' => $fake_student->gender,
                'grade' => $fake_student->grade ? $fake_student->grade : '',
                'name' => $fake_student->name,
                'nickname' => $fake_student->nickname ? $fake_student->nickname : '',
            ]);

        $volunteer = FakeFactory::fakeVolunteer();
        $this->actingAs($volunteer->user, 'api')
            ->json('GET', '/students/' . $fake_student->id)
            ->seeJsonError(403);

        $student = FakeFactory::fakeStudent();
        $this->actingAs($student->user, 'api')
            ->json('GET', '/students/' . $fake_student->id)
            ->seeJsonError(403);
    }

    public function testStore()
    {
        $school = FakeFactory::fakeSchool();

        $admin = FakeFactory::fakeAdmin();
        $this->actingAs($admin->user, 'api')
            ->json('POST', '/students', [
                'school_id' => $school->id,
                'name' => 'Mark Chen',
                'grade' => 'One',
                'gender' => '女',
            ])
            ->seeJson([
                'school_id' => $school->id,
                'name' => 'Mark Chen',
                'grade' => 'One',
                'gender' => '女',
            ])
            ->seeJsonStructure([
                'school',
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertEquals(\App\Student::find($json['id'])->gender, $json['gender']);
            });

        $volunteer = FakeFactory::fakeVolunteer();
        $this->actingAs($volunteer->user, 'api')
            ->json('POST', '/students')
            ->seeJsonError(403);

        $student = FakeFactory::fakeStudent();
        $this->actingAs($student->user, 'api')
            ->json('POST', '/students')
            ->seeJsonError(403);
    }

    public function testStoreValidationError()
    {
        $admin = FakeFactory::fakeAdmin();

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/students')
            ->seeJsonError(422);

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/students', ['school_id' => -1])
            ->seeJsonError(422);

        $this->actingAs($admin->user, 'api')
            ->json('POST', '/students', [
                'name' => 'Mark Chen',
                'grade' => 'One',
                'gender' => 'MAN',
            ])
            ->seeJsonError(422);
    }

    public function testStoreWithPhoto()
    {
        $school = FakeFactory::fakeSchool();

        $admin = FakeFactory::fakeAdmin();

        $imgFile = $this->createImageFile();

        $this->actingAs($admin->user, 'api')
            ->jsonWithFiles('POST', '/students', [
                'school_id' => $school->id,
                'name' => 'Mark Chen',
                'grade' => 'One',
                'gender' => '女',
            ], [], ['photo_file' => $imgFile])
            ->seeJson([
                'school_id' => $school->id,
                'name' => 'Mark Chen',
                'grade' => 'One',
                'gender' => '女',
            ])
            ->seeJsonStructure([
                'school',
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }

    public function testUpdateByAdmin()
    {
        $admin = FakeFactory::fakeAdmin();
        $student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $this->actingAs($admin->user, 'api')
            ->json('PATCH', '/students/'.$student->id, [
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJson([
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJsonStructure([
                'school',
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNull($json['photo']);
            });
    }

    public function testUpdateByOtherStudent()
    {
        $student = FakeFactory::fakeStudent();
        $other_student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $this->actingAs($other_student->user, 'api')
            ->json('PATCH', '/students/'.$student->id, [
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJsonError(403);
    }

    public function testUpdateByOwner()
    {
        $student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $this->actingAs($student->user, 'api')
            ->json('PATCH', '/students/'.$student->id, [
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJson([
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJsonStructure([
                'school',
                'user',
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNull($json['photo']);
            });
    }

    public function testUpdateWithPhoto()
    {
        $admin = FakeFactory::fakeAdmin();
        $student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $imgFile = $this->createImageFile();

        $this->actingAs($admin->user, 'api')
            ->jsonWithFiles('PATCH', '/students/'.$student->id, [
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ], [], ['photo_file' => $imgFile])
            ->seeJson([
                'school_id' => $school->id,
                'name' => 'Mark',
                'grade' => 'Two',
            ])
            ->seeJsonStructure([
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }

    public function testUpdatePhotoByAdmin()
    {
        $admin = FakeFactory::fakeAdmin();
        $student = FakeFactory::fakeStudent();

        $imgFile = $this->createImageFile();

        $this->actingAs($admin->user, 'api')
            ->jsonWithFiles('POST', '/students/'.$student->id.'/photo', [], [], ['photo_file' => $imgFile])
            ->seeJson([
                'id' => $student->id,
                'school_id' => $student->school->id,
            ])
            ->seeJsonStructure([
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }

    public function testUpdatePhotoByOtherStudent()
    {
        $student = FakeFactory::fakeStudent();

        $other_student = FakeFactory::fakeStudent();

        $imgFile = $this->createImageFile();

        $this->actingAs($other_student->user, 'api')
            ->jsonWithFiles('POST', '/students/'.$student->id.'/photo', [], [], ['photo_file' => $imgFile])
            ->seeJsonError(403);
    }

    public function testUpdatePhotoByOwner()
    {
        $student = FakeFactory::fakeStudent();

        $imgFile = $this->createImageFile();

        $this->actingAs($student->user, 'api')
            ->jsonWithFiles('POST', '/students/'.$student->id.'/photo', [], [], ['photo_file' => $imgFile])
            ->seeJson([
                'id' => $student->id,
                'school_id' => $student->school->id,
            ])
            ->seeJsonStructure([
                'photo',
            ])
            ->seeDecodedJson(function ($json) {
                $this->assertNotNull($json['photo']);
            });
    }

    public function testUpdateStatusByAdmin()
    {
        $admin = FakeFactory::fakeAdmin();
        $student = FakeFactory::fakeStudent();

        $this->actingAs($admin->user, 'api')
            ->json('PATCH', '/students/'.$student->id.'/status', [
                'status' => '3',
            ])
            ->seeJson([
                'status' => '3',
            ])
            ->seeDecodedJson(function ($json) use ($student) {
                $this->assertEquals(\App\Student::find($student->id)->status, $json['status']);
            });
    }

    public function testUpdateStatusByOtherStudent()
    {
        $student = FakeFactory::fakeStudent();
        $other_student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $this->actingAs($other_student->user, 'api')
            ->json('PATCH', '/students/'.$student->id.'/status', [
                'status' => '3',
            ])
            ->seeJsonError(403);
    }

    public function testUpdateStatusByOwner()
    {
        $student = FakeFactory::fakeStudent();
        $school = FakeFactory::fakeSchool();

        $this->actingAs($student->user, 'api')
            ->json('PATCH', '/students/'.$student->id.'/status', [
                'status' => '3',
            ])
            ->seeJsonError(403);
    }
}
