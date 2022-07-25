package io.alliance.adn.util;

import io.alliance.adn.element.*;
import io.alliance.adn.exception.InvalidCastException;
import org.jetbrains.annotations.NotNull;

public final class Caster {

    @NotNull
    public static ADNBoolean toBoolean(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.BOOLEAN)) {
            return (ADNBoolean) element;
        }

        throw new InvalidCastException(element, ADNType.BOOLEAN);
    }

    @NotNull
    public static ADNInt8 toInt8(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.INT8)) {
            return (ADNInt8) element;
        }

        throw new InvalidCastException(element, ADNType.INT8);
    }

    @NotNull
    public static ADNInt16 toInt16(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.INT16)) {
            return (ADNInt16) element;
        }

        throw new InvalidCastException(element, ADNType.INT16);
    }

    @NotNull
    public static ADNInt32 toInt32(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.INT32)) {
            return (ADNInt32) element;
        }

        throw new InvalidCastException(element, ADNType.INT32);
    }

    @NotNull
    public static ADNInt64 toInt64(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.INT64)) {
            return (ADNInt64) element;
        }

        throw new InvalidCastException(element, ADNType.INT64);
    }

    @NotNull
    public static ADNFP32 toFloat32(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.FP32)) {
            return (ADNFP32) element;
        }

        throw new InvalidCastException(element, ADNType.FP32);
    }

    @NotNull
    public static ADNFP64 toFloat64(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.FP64)) {
            return (ADNFP64) element;
        }

        throw new InvalidCastException(element, ADNType.FP64);
    }

    @NotNull
    public static ADNString toString(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.STRING)) {
            return (ADNString) element;
        }

        throw new InvalidCastException(element, ADNType.STRING);
    }

    @NotNull
    public static ADNObject toObject(@NotNull ADNElement element) throws InvalidCastException {
        if(element.typeOf(ADNType.OBJECT)) {
            return (ADNObject) element;
        }

        throw new InvalidCastException(element, ADNType.OBJECT);
    }
}