
package cn.dceast.platform.paas.api.resource;

import cn.dceast.platform.paas.api.MediaTypes;
import cn.dceast.platform.paas.model.Home;
import cn.dceast.platform.paas.model.User;
import cn.dceast.platform.paas.service.HomeService;
import cn.dceast.platform.paas.utils.Constants;
import cn.dceast.platform.paas.utils.jsonUtil.JsonCommon;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhming on 2015/10/30.
 */

/**
 * 首页接口
 * 包含：bannerImgUrl（大图）、quickAnswerImgUrl（快速解答）、myAnswerImgUrl（我的回答）、findEngineerImgUrl（找工程师）
 *
 * 1、 value， method；
 * value：     指定请求的实际地址，指定的地址可以是URI Template 模式（后面将会说明）；
 * method：  指定请求的method类型， GET、POST、PUT、DELETE等；
 * 2、 consumes，produces；
 * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
 * produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
 * 3、 params，headers；
 * params： 指定request中必须包含某些参数值是，才让该方法处理。
 * headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。
 */
@RestController
@RequestMapping(value = "/api/home")
public class HomeResource {
    private static final Log log = LogFactory.getLog(HomeResource.class);
    @Autowired
    private HomeService homeService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 切面处理异常情况
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Object list(){
        try{
            List<Home> list = homeService.getHome();
            //TODO get question list
            //统一格式返回
            return new JsonCommon(Constants.JV_SUCCESS,list);
        }catch(Exception e){
            return new JsonCommon(Constants.JV_ERROR_DATABASE,null);
        }
    }

//    /**
//     * 根据userId获取用户信息
//     * @param userId
//     * @return
//     */
//    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
//    public Object getById(@PathVariable("id") String userId) {
//        try{
//            User user = homeService.getUserById(userId);
//            if (user == null) {
//                String message = "用户信息不存在(userId:" + userId + ")";
//                log.debug(message);
//                return new JsonCommon(Constants.JV_SUCCESS_NULL,"");
//            }
//            return new JsonCommon(Constants.JV_SUCCESS,user);
//        }catch(Exception e){
//            log.error(e);
//            return new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//        }
//    }
//
//    /**
//     * 根据userName获取用户信息
//     * @param userName
//     * @return
//     */
//    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
//    public Object getByName(@PathVariable("name") String userName) {
//        try{
//            User user = homeService.getUserByName(userName);
//            if (user == null) {
//                String message = "用户信息不存在(userName:" + userName + ")";
//                log.debug(message);
//                return new JsonCommon(Constants.JV_SUCCESS_NULL,"");
//            }
//            return new JsonCommon(Constants.JV_SUCCESS,user);
//        }catch(Exception e){
//            //错误日志记录由切面管理
////            log.error(e);
//            e.printStackTrace();
//            return new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//        }
//    }
//
//    /**
//     *
//     * @param userName
//     * @param password
//     * select user by userName and password
//     * @return
//     */
//    @RequestMapping(value = "/login",method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
//    public Object login(@RequestParam(value = "userName",required = true)String userName,
//                      @RequestParam(value = "password",required = true)String password) {
//        User user = null;
//        try{
//            user = homeService.getUserByNameAndPwd(userName,password);
//        }catch (Exception e){
//            log.error(e);
//            return new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//        }
//        //用户不存在
//        if(user == null){
//            return new JsonCommon(Constants.JV_SUCCESS_NULL,"");
//        }
//        return new JsonCommon(Constants.JV_SUCCESS,user);
//    }
//
//    /**
//     * 新增注册用户
//     * 插入成功后，http相应头（header）中定位访问用户信息地址uri
//     * @param user
//     * @param uriBuilder
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
//    public ResponseEntity<?> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
//
//        String userName = user.getUserName();
//        try{
//            //1,去重userName TODO 后期做切面
//            User userRes = homeService.getUserByName(userName);
//            if(userRes != null){
//                HttpHeaders headers = new HttpHeaders();
//                JsonCommon jc1 = new JsonCommon(Constants.JV_SUCCESS_REPEAT_NAME,"");
//                return new ResponseEntity(jc1, headers, HttpStatus.OK);
//            }
//            //插入数据库
//            homeService.addUser(user);
//        }catch(Exception e){
//            log.debug(e);
//            HttpHeaders headers = new HttpHeaders();
//            JsonCommon jc2 = new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//            return new ResponseEntity(jc2, headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        // 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
//        URI uri = uriBuilder.path("/api/users/name/" + userName).build().toUri();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(uri);
//
//        JsonCommon jc3 = new JsonCommon(Constants.JV_SUCCESS,"");
//        return new ResponseEntity(jc3, headers, HttpStatus.CREATED);
//    }
//
//    /**
//     * 修改登录密码
//     * @param user
//     */
//    @RequestMapping(method = RequestMethod.PUT, consumes = MediaTypes.JSON_UTF_8,produces = MediaTypes.JSON_UTF_8)
//    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
////    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Object update(@RequestBody User user) {
//        try{
//            homeService.updateUser(user);
//        }catch (Throwable throwable){
//            log.error(throwable);
//            throwable.printStackTrace();
//            return new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//        }
//        return new JsonCommon(Constants.JV_SUCCESS,"");
//    }
//
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE,produces = MediaTypes.JSON_UTF_8)
////    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Object delete(@PathVariable("id") String userId) {
//        try{
//            homeService.deleteUser(userId);
//        }catch (Throwable throwable){
//            log.error(throwable);
//            return new JsonCommon(Constants.JV_ERROR_DATABASE,"");
//        }
//        return new JsonCommon(Constants.JV_SUCCESS,"");
//    }
}