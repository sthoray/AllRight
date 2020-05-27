import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.SearchResultsActivity

class SearchResultsAdapter: RecyclerView.Adapter<SearchResultsActivity.SearchResultsViewHolder>(){
    override fun getItemCount(): Int {
        return 20
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsActivity.SearchResultsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.search_results_row, parent, false)
        return SearchResultsActivity.SearchResultsViewHolder(view)
    }
//
    override fun onBindViewHolder(holder: SearchResultsActivity.SearchResultsViewHolder, position: Int) {

    }
}