(function (factory) {
	// The usual JS module definition dance.
    if ( typeof define === 'function' && define.amd ) {
        // AMD
        define([], factory);
    } else if ( typeof exports === 'object' ) {
        // Node/CommonJS
        module.exports = factory();
    } else {
        // Browser global
        window.MinMaxSlider = factory();
    }

}(function(){

	'use strict';

	function _create(container, inputs, bounds, start) {
		// The difference between the upper and lower bound.
		var delta = bounds[1] - bounds[0];
		// Set the input control limits.
		for (var i=0; i < inputs.length; i++) {
			inputs[i].min = bounds[0];
			inputs[i].max = bounds[1];
		}
		// The number of significant digits in the delta.
		var deltaDigitsCnt = delta.toString().length;
		// The unadjusted intermediate range interval size,
		// e.g. 100 for bounds [0, 500], 10 for bounds [20, 100],
		// 1 for bounds [1, 10].
		var intervalSize = Math.pow(10, deltaDigitsCnt - 1);
		// The unadjusted range interval count, e.g. 1 for bounds
		// [0, 10], 2 for bounds [400, 600], 3 for bounds [12, 40].
		var intervalCnt = Math.ceil(delta / intervalSize);
		// Adjust to ensure that there are at least five labels,
		// i.e. one min, one max and three intermediate labels.
		// Expand an interval count of 1 to 5, so, e.g., bounds
		// [1, 10] will have labels at [1, 2, 4, 6, 8, 10].
		// Expand an interval count of 2 to 4, so. e.g., bounds
		// [400, 600] will have labels at [400, 450, 500, 550, 600].
		if (intervalCnt < 3 && intervalSize > 1) {
			var resize = intervalCnt === 1 ? 5 : 2;
			intervalCnt *= resize;
			intervalSize /= resize;
		}
		var points = Array(intervalCnt + 1);
		points[0] = bounds[0];
		points[1] = bounds[0] + intervalSize - (bounds[0] % intervalSize);
		for (var i=2; i < intervalCnt; i++) {
			points[i] = points[i - 1] + intervalSize;
		}
		points[intervalCnt] = bounds[1];
		var ranges = points.map(function (p, i) { 
			// The difference between the current and next point.
			var d = i === intervalCnt ? 0 : points[i + 1] - p;
			// The slider step is a power of 10, e.g. 1 for interval
			// [10, 20], 10 for interval [100, 200], 100 for interval
			// [1000, 2000]. The first interval (from min) is a finer
			// step, e.g. 1 for interval [0, 100], as set by the
			// factor variable.
			var factor = i === 0 ? 3 : 2;
			var step = Math.max(1, Math.pow(10, d.toString().length - factor));
			return [p, step];
		});
		var range = {
			'min': ranges[0],
			'max': ranges[intervalCnt]
		};
		for (var i=1; i < intervalCnt; i++) {
			var pct = Math.floor((points[i] - bounds[0]) * 100 / delta).toString() + '%';
			range[pct] = ranges[i];
		}
		// Make the slider.
		var slider = noUiSlider.create(container, {
			start: start,
			connect: true,
			range: range,
			pips: {
				mode: 'range',
				density: 2
			}
		});
		
		// Add value coordination handlers.
		inputs[0].onchange = function() {
			var value = parseInt(this.value);
			slider.set([value, slider.get()[1]]);
		};
		inputs[1].onchange = function() {
			var value = parseInt(this.value);
			slider.set([slider.get()[0], value]);
		};
		slider.on('start', function(values, handle) {
			this._isActive = true;
		});
		slider.on('end', function(values, handle) {
			this._isActive = false;
		});
		slider.on('update', function(values, handle) {
			if (this._isActive !== false) {
				inputs[handle].value = Math.round(values[handle]);
			}
		});
		
		// Add the values convenience accessor.
		slider.getValues = slider.target.noUiSlider.get;
		
		return slider;
	}
	
	function _getValues(element) {
		// noUiSlider has float values. Convert these to integers.
		return element.noUiSlider.get().map(Math.round);
	}

	return {
		create: _create,
		getValues: _getValues
	}

}));