package com.easypan.aspect;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.enums.ResponseCodeEnum;
import com.easypan.exception.BusinessException;
import com.easypan.utils.StringTools;
import com.easypan.utils.VerifyUtils;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.easypan.entity.constants.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.nimbus.NimbusStyle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.springframework.cglib.core.Constants.TYPE_LONG;
import static org.springframework.cglib.core.Constants.TYPE_STRING;

@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    public static final Logger logger= LoggerFactory.getLogger(GlobalOperationAspect.class);

    public static final String TAYE_STRING="java.lang.String";
    public static final String TAYE_INTEGER="java.lang.Integer";
    public static final String TAYE_LONG="java.lang.Long";

    @Pointcut ("@annotation(com.easypan.annotation.GlobalInterceptor)")
    private  void requestInterceptor(){

    }
    @Before("requestInterceptor()")
    public  void interceptorDo(JoinPoint point) throws BusinessException{
       try {
           Object target =point.getTarget();
           Object[] arguments =point.getArgs();
           String methodName =point.getSignature().getName();
           Class<?> [] paramterTypes=((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
           Method method =target.getClass().getMethod(methodName,paramterTypes);
           GlobalInterceptor interceptor =method.getAnnotation(GlobalInterceptor.class);
           if(interceptor==null){
               return;
           }
            if(interceptor.checkLogin() || interceptor.checkAdmin()){
                checkLogin(interceptor.checkAdmin());
            }
           /**
            * 校验参数
            */
           if(interceptor.checkParams()){
               validateParams(method,arguments);
           }

       }catch (BusinessException e){
           logger.error("全局拦截器异常",e);
           throw  e;
       }catch (Exception e ){
           logger.error("全局拦截器异常",e);
           throw new BusinessException(ResponseCodeEnum.CODE_500);
       }catch (Throwable e ){
           logger.error("全局拦截器异常",e);
           throw new BusinessException(ResponseCodeEnum.CODE_500);
       }
    }
    private void checkLogin(Boolean checkAdmin){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =request.getSession();
        SessionWebUserDto userDto =(SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        if(null == userDto){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if(checkAdmin && !userDto.getAdmin()){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }

    private void validateParams(Method m, Object[] arguments) throws BusinessException{
        Parameter [] parameters =m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter=parameters[i];
            Object value=arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
             if(verifyParam ==null){
                continue;
            }
            if(TYPE_STRING.equals(parameter.getParameterizedType().getTypeName())||TYPE_LONG.equals(parameter.getParameterizedType().getTypeName())){
                checkValue(value,verifyParam);
            }else {
                checkValue(value,verifyParam);
                //checkObjValue(parameter,value);
            }

        }
    }

    private  void  checkObjValue(Parameter parameter,Object value){
        try {
            String typeName =parameter.getParameterizedType().getTypeName();
            Class classz =Class.forName(typeName);
            Field[] fields =classz.getDeclaredFields();
            for(Field field : fields){
                VerifyParam fieldVerifyParam =field.getAnnotation(VerifyParam.class);
                if(fieldVerifyParam ==null){
                    continue;
                }
                field.setAccessible(true);
                Object resultValue =field.get(value);
                checkValue(resultValue,fieldVerifyParam);
            }

    }catch (BusinessException e){
            logger.error("校验参数失败",e);
            throw  e ;
        }catch (Exception e ){
            logger.error("校验参数失败",e);
            throw  new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    private  void checkValue (Object value,VerifyParam verifyParam) throws BusinessException{
        Boolean isEmpty = value ==null || StringTools.isEmpty(value.toString());
        Integer length =value ==null  ? 0:value.toString().length();
        /**
         * 校验空
         */
        if(isEmpty && verifyParam.required())
        {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /**
         * 校验长度
         */

        if(!isEmpty && (verifyParam.max()!=-1 && verifyParam.max()<length || verifyParam.min() !=-1 && verifyParam.min()>length ))
        {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /**
         * 校验正则
         */
        if(!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex(),String.valueOf(value)))
        {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

    }


}
