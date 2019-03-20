package com.channelwin.ssc;

import com.channelwin.ssc.QuestionWarehouse.model.validator.ValidateIgnore;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public abstract class Validatable {
    final public void validate() {
        log.info(this + " 单项校验");
        // 单项校验
        Set<ConstraintViolation<Validatable>> violations = Validation.buildDefaultValidatorFactory().getValidator()
                .validate(this);

        if (violations.size()> 0 ) {
            throw new ValidateException(violations.toString());
        }

        // 单项Validatable校验, ValidateIgnore的忽略
        Validatable.validateObjectValidatableFields(this);

        // 整体校验
        this.typeValidate();
    }

    public void typeValidate() {
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static void validateObjectValidatableFields(Validatable validatable) {
        if (validatable == null) {
            return;
        }

        List<Field> fields = getAllFields(new LinkedList<Field>(), validatable.getClass());

        int i = 0;
        for (Field item : fields) {
            i++;
            // 处理ValidateIgnore
            if (item.isAnnotationPresent(ValidateIgnore.class)) {
                continue;
            }

            try {
                item.setAccessible(true);
                Object obj = item.get(validatable);

                // 空项跳过
                if (obj == null) {
                    continue;
                }

                // String跳过
                if (obj instanceof String) {
                    continue;
                }

                if (obj instanceof Validatable) {
                    log.info("验证:" + item.getName() +":" + obj);
                    // 约定Validatable的对象一定是连续嵌套的, 不存在(Validatable 中含有 非Validatable, 但是非Validatable中又含有Validatable)
                    ((Validatable) obj).validate();
                } else {
                    // 判断是否是集合
                    Class<?> clazz = item.getType();
                    if (Collection.class.isAssignableFrom(clazz)) {
                        // Collection
                        // 判断元素是否是Validatable
                        Type gType = item.getGenericType();

                        ParameterizedType pType = (ParameterizedType) gType;

                        // 取得泛型类型的泛型参数
                        Class<?> typeClass = (Class<?>) (pType.getActualTypeArguments()[0]);

                        if (Validatable.class.isAssignableFrom(typeClass)) {
                            // 校验
                            try {
                                item.setAccessible(true);
                                Collection<Validatable> collection = (Collection<Validatable>) (item.get(validatable));
                                for (Validatable validatableItem : collection) {
                                    validatableItem.validate();
                                }
                            } catch (Exception e) {
                                throw new ValidateException(e.toString());
                            }
                        }

                    } else if (Map.class.isAssignableFrom(clazz)) {
                        // Map
                        // 判断元素是否是Validatable
                        Type gType = item.getGenericType();

                        ParameterizedType pType = (ParameterizedType) gType;

                        // 取得泛型类型的泛型参数
                        Class<?> keyTypeClass = (Class<?>) (pType.getActualTypeArguments()[0]);
                        if (Validatable.class.isAssignableFrom(keyTypeClass)) {
                            // 校验
                            try {
                                item.setAccessible(true);
                                Map<Validatable, Object> map = (Map) (item.get(validatable));
                                for (Entry<Validatable, Object> entry : map.entrySet()) {
                                    entry.getKey().validate();
                                }
                            } catch (Exception e) {
                                throw new ValidateException(e.toString());
                            }
                        }

                        Class<?> valueTypeClass = (Class<?>) (pType.getActualTypeArguments()[1]);
                        if (Validatable.class.isAssignableFrom(valueTypeClass)) {
                            // 校验
                            try {
                                item.setAccessible(true);
                                Map<Object, Validatable> map = (Map) (item.get(validatable));
                                for (Entry<Object, Validatable> entry : map.entrySet()) {
                                    entry.getValue().validate();
                                }
                            } catch (Exception e) {
                                throw new ValidateException(e.toString());
                            }
                        }
                    } else if (clazz.isArray()) {
                        // array
                        // 判断元素是否是Validatable
                        Type gType = item.getGenericType();

                        // 取得数组元素类型
                        Class<?> typeClass = ((Class<?>) gType).getComponentType();

                        if (Validatable.class.isAssignableFrom(typeClass)) {
                            // 校验
                            try {
                                item.setAccessible(true);
                                Validatable[] validatables = (Validatable[]) (item.get(validatable));
                                for (Validatable validatableItem : validatables) {
                                    validatableItem.validate();
                                }
                            } catch (Exception e) {
                                throw new ValidateException(e.toString());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new ValidateException(e.toString());
            }
        }
    }
}
