package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import br.com.mrcontador.file.comprovante.DiffText;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.file.comprovante.ParserComprovante;

public abstract class ComprovanteBanco implements ParserComprovante{
	
	public List<DiffValue> parse(String comprovante, String pattern) throws DiffException{
		DiffText d = new DiffText();
		Map<Integer, DiffValue> map = d.doDiff(pattern,comprovante);
		List<DiffValue> list = new ArrayList<DiffValue>();
		for (DiffValue _values : map.values()) {
			diffpatch(list,_values.getOldValue(), _values.getNewValue());
		}
		return list;
	}
	
	private void diffpatch(List<DiffValue> list , String original, String revision) throws DiffException {
		if(original == null || revision == null) {
			return;
		}
		Patch<String> patchs = DiffUtils.diffInline(original, revision);
		List<AbstractDelta<String>> deltas = patchs.getDeltas();
		for (AbstractDelta<String> delta : deltas) {
			if(delta.getType().equals(DeltaType.CHANGE)){
				DiffValue diffValues = new DiffValue();
				diffValues.setOldValue(delta.getSource().getLines().get(0));
				diffValues.setNewValue(delta.getTarget().getLines().get(0));
				list.add(diffValues);
			}
			if(delta.getType().equals(DeltaType.INSERT)) {
				if(!list.isEmpty()) {
				list.get(list.size()-1).setNewValue(list.get(list.size()-1).getNewValue().concat(" ").concat(delta.getTarget().getLines().get(0)));
				}
			}
			
		}
	}
	
	

}
