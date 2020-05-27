import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.SearchActivity

class SearchResultsAdapter: RecyclerView.Adapter<SearchActivity.SearchResultsViewHolder>(){
    override fun getItemCount(): Int {
        return 20
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchActivity.SearchResultsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.search_result_row, parent, false)
        return SearchActivity.SearchResultsViewHolder(view)
    }
//
    override fun onBindViewHolder(holder: SearchActivity.SearchResultsViewHolder, position: Int) {

    }
}