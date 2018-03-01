<?php

namespace App\Http\Controllers;

use Auth;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Routing\Controller as BaseController;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;

// API Doc Definition

/**
 * @apiDefine StudentParam 參數 (學生身份)
 */

/**
 * @apiDefine VolunteerParam 參數 (志工身份)
 */

/**
 * @apiDefine StudentScope Success 200 (學生身份)
 */

/**
 * @apiDefine VolunteerScope Success 200 (志工身份)
 */

/**
 * @apiDefine UnauthorizedError
 * @apiError Unauthorized `401` 需要登入、或合法的 token
 */

/**
 * @apiDefine ForbiddenError
 * @apiError Forbidden `403` 權限不合法
 */

/**
 * @apiDefine ValidationError
 * @apiError UnprocessableEntity `422` 欄位錯誤
 */

/**
 * @apiDefine ResourceError
 * @apiError Conflict `409` 資源發生衝突
 */

/**
 * @apiDefine NotFoundError
 * @apiError NotFound `404` 找不到資源
 */

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    /**
     * {@inheritdoc}
     */
    protected function formatValidationErrors(Validator $validator)
    {
        return [
            'error' => [
                'detail' => $validator->errors()->getMessages(),
            ],
        ];
    }
}
