package com.library.mylibrary.colorpicker.builder;



import com.library.mylibrary.colorpicker.ColorPickerView;
import com.library.mylibrary.colorpicker.renderer.ColorWheelRenderer;
import com.library.mylibrary.colorpicker.renderer.FlowerColorWheelRenderer;
import com.library.mylibrary.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
	public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
		switch (wheelType) {
			case CIRCLE:
				return new SimpleColorWheelRenderer();
			case FLOWER:
				return new FlowerColorWheelRenderer();
		}
		throw new IllegalArgumentException("wrong WHEEL_TYPE");
	}
}