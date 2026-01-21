package com.ocosur.ocosystem.security;

import java.lang.reflect.Method;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
@Aspect
@Component
@RequiredArgsConstructor
public class BusinessAccessAspect {

    private final BusinessAccessService service;

    @Before("execution(* *(..)) && (@within(com.ocosur.ocosystem.security.RequireBusiness) || @annotation(com.ocosur.ocosystem.security.RequireBusiness))")
    public void checkAccess(JoinPoint joinPoint) {

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        // 1️⃣ Primero intenta en el método
        RequireBusiness requireBusiness =
                method.getAnnotation(RequireBusiness.class);

        // 2️⃣ Si no existe, busca en la clase
        if (requireBusiness == null) {
            Class<?> targetClass =
                    joinPoint.getTarget().getClass();

            requireBusiness =
                    targetClass.getAnnotation(RequireBusiness.class);
        }

        // 3️⃣ Si no hay anotación → no aplica seguridad
        if (requireBusiness == null) {
            return;
        }

        String businessCode = requireBusiness.value();

        boolean allowed =
                service.hasBusinessAccess(businessCode);

        if (!allowed) {
            throw new AccessDeniedException(
                    "No access to business " + businessCode
            );
        }
    }
}
