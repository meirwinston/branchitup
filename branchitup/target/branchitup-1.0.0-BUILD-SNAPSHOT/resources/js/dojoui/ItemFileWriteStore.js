if(!dojo._hasResource["dojoui.ItemFileWriteStore"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.ItemFileWriteStore"] = true;
dojo.provide("dojoui.ItemFileWriteStore");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.declare("dojoui.ItemFileWriteStore", dojo.data.ItemFileWriteStore, {
	/*
	 * adding element to a JSON object like metadata causing an error (in treegrid)
	 * because the store assumes all elements are arrays
	 * I am overwriting this method with a fix to allow objects as well
	 */
	_setValueOrValues: function (_28, _29, _2a, _2b) { 
		this._assert(!this._saveInProgress); 
		this._assertIsItem(_28); 
		this._assert(dojo.isString(_29)); 
		this._assert(typeof _2a !== "undefined"); 
		var _2c = this._getIdentifierAttribute(); 
		if (_29 == _2c) { 
			throw new Error("ItemFileWriteStore does not have support for changing the value of an item's identifier."); 
		} 
		var _2d = this._getValueOrValues(_28, _29); 
		var _2e = this.getIdentity(_28); 
		if (!this._pending._modifiedItems[_2e]) { 
			var _2f = {}; 
			for (var key in _28) { 
				if (key === this._storeRefPropName || key === this._itemNumPropName || key === this._rootItemPropName) { 
					_2f[key] = _28[key]; 
				} 
				else { 
					if (key === this._reverseRefMap) { 
						_2f[key] = dojo.clone(_28[key]); 
					} 
					else 
					{ //^^ this block is intended to support attributes of type object
						if(_28[key].constructor == Array){ //^^
							_2f[key] = _28[key].slice(0, _28[key].length);
						}
						else{
							_2f[key] = _28[key];
						}
					} 
				} 
			} 
			this._pending._modifiedItems[_2e] = _2f; 
		} 
		var _30 = false; 
		if (dojo.isArray(_2a) && _2a.length === 0) { 
			_30 = delete _28[_29]; 
			_2a = undefined; 
			if (this.referenceIntegrity && _2d) { 
				var _31 = _2d; 
				if (!dojo.isArray(_31)) { 
					_31 = [_31]; 
				} 
				for (var i = 0; i < _31.length; i++) { 
					var _32 = _31[i]; 
					if (this.isItem(_32)) { 
						this._removeReferenceFromMap(_32, _28, _29); 
					} 
				} 
			} 
		} 
		else { 
			var _33; 
			if (dojo.isArray(_2a)) { 
				var _34 = _2a; 
				_33 = _2a.slice(0, _2a.length); 
			} 
			else { 
				_33 = [_2a]; 
			} 
			if (this.referenceIntegrity) { 
				if (_2d) { 
					var _31 = _2d; 
					if (!dojo.isArray(_31)) { 
						_31 = [_31]; 
					} 
					var map = {}; 
					dojo.forEach(_31, function (_35) {
						if (this.isItem(_35)) {
							var id = this.getIdentity(_35);
							map[id.toString()] = true;
						}
					},this); 
					dojo.forEach(_33, function (_36) {
						if (this.isItem(_36)) {
							var id = this.getIdentity(_36);
							if (map[id.toString()]) {
								delete map[id.toString()];
							} 
							else {
								this._addReferenceToMap(_36, _28, _29);
							}
						}
					}, this); 
					for (var rId in map) { 
						var _37; 
						if (this._itemsByIdentity) { 
							_37 = this._itemsByIdentity[rId]; 
						} 
						else { 
							_37 = this._arrayOfAllItems[rId]; 
						} 
						this._removeReferenceFromMap(_37, _28, _29); 
					} 
				} 
				else { 
					for (var i = 0; i < _33.length; i++) { 
						var _32 = _33[i]; 
						if (this.isItem(_32)) { 
							this._addReferenceToMap(_32, _28, _29); 
						} 
					} 
				} 
			} 
			_28[_29] = _33; 
			_30 = true; 
		} 
		if (_2b) { 
			this.onSet(_28, _29, _2d, _2a); 
		} 
		return _30; 
	}
});
}
