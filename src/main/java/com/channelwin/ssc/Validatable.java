package com.channelwin.ssc;

import com.channelwin.ssc.QuestionWarehouse.model.validator.ValidateIgnore;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public abstract class Validatable {
    public void validate() {
        log.info(this.toString() + " validate");
    }

    public static void validateObjectValidatableFields(Object object) {
        log.info("开始检查:" + object);
        if (object == null) {
            return;
        }

        Class<?> objectClazz = object.getClass();

        if (Collection.class.isAssignableFrom(objectClazz)) {
            // 当前对象是Collection
            for (Object item : (Collection) object) {
                validateObjectValidatableFields(item);
            }
        } else if (Map.class.isAssignableFrom(objectClazz)) {
            // 当前对象是Map
            for (Entry<Object, Object> item : ((Map<Object, Object>) object).entrySet()) {
                validateObjectValidatableFields(item.getKey());
                validateObjectValidatableFields(item.getValue());
            }
        } else if (objectClazz.isArray()) {
            // 当前对象是array
            for (Object item : (Object[]) object) {
                validateObjectValidatableFields(item);
            }
        } else {
            // 当前对象是单项
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field item : fields) {
                // 处理ValidateIgnore
                if (item.isAnnotationPresent(ValidateIgnore.class)) {
                    continue;
                }

                try {
                    item.setAccessible(true);
                    Object obj = item.get(object);

                    if (obj == null) {
                        continue;
                    }

                    if (obj instanceof Validatable) {
                        log.info("验证:" + item.getName());
                        // todo 约定Validatable的对象一定是连续嵌套的, 不存在(Validatable 中含有 非Validatable, 但是非Validatable中又含有Validatable)
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
                                    Collection<Validatable> collection = (Collection<Validatable>) (item.get(object));
                                    validateObjectValidatableFields(collection);
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
                            Class<?> valueTypeClass = (Class<?>) (pType.getActualTypeArguments()[1]);

                            // 校验
                            try {
                                item.setAccessible(true);
                                Map<Object, Object> map = (Map) (item.get(object));
                                for (Entry<Object, Object> entry : map.entrySet()) {
                                    validateObjectValidatableFields(entry.getKey());
                                    validateObjectValidatableFields(entry.getValue());
                                }
                            } catch (Exception e) {
                                throw new ValidateException(e.toString());
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
                                    Validatable[] validatables = (Validatable[]) (item.get(object));
                                    validateObjectValidatableFields(validatables);
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
}
